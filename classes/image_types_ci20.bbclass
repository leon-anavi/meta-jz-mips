inherit image_types

IMAGE_BOOTLOADER ?= "u-boot-ci20"

# Handle u-boot suffixes
UBOOT_SUFFIX ?= "img"
UBOOT_SUFFIX_SDCARD ?= "${UBOOT_SUFFIX}"

#BOOT components
UBOOT_SPL_POS ?= "1"
UBOOT_BIN_POS ?= "14"

# Boot partition volume id
BOOTDD_VOLUME_ID ?= "${MACHINE}"

# Set alignment to 4MB [in KiB]
IMAGE_ROOTFS_ALIGNMENT = "2048"

SDIMG_ROOTFS_TYPE ?= "ext4"
SDIMG_ROOTFS = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.${SDIMG_ROOTFS_TYPE}"

# Boot partition size [in KiB]
BOOT_SPACE ?= "102400"

IMAGE_DEPENDS_sdcard = "parted-native:do_populate_sysroot \
                        dosfstools-native:do_populate_sysroot \
                        mtools-native:do_populate_sysroot \
                        virtual/kernel:do_deploy \ 
                        ${@d.getVar('IMAGE_BOOTLOADER', True) and d.getVar('IMAGE_BOOTLOADER', True) + ':do_deploy' or ''}"

SDCARD = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.sdcard"
SDCARD_GENERATION_COMMAND_creator-ci20 = "generate_ci20_sdcard"

#
generate_ci20_sdcard () {
	# Create partition table
    parted -s ${SDCARD} mklabel msdos

    # Create rootfs partition to the end of disk
    parted -s ${SDCARD} -- unit KiB mkpart primary ext2 ${IMAGE_ROOTFS_ALIGNMENT} -1s
    parted ${SDCARD} print
    case "${IMAGE_BOOTLOADER}" in
        u-boot-ci20)
            dd if=${DEPLOY_DIR_IMAGE}/u-boot-spl.bin of=${SDCARD} obs=512 seek=${UBOOT_SPL_POS}
            dd if=${DEPLOY_DIR_IMAGE}/u-boot.${UBOOT_SUFFIX} of=${SDCARD} obs=1k seek=${UBOOT_BIN_POS}
            dd if=/dev/zero of=${SDCARD} seek=526  count=32 bs=1k
        ;;
        *)
            bberror "Unknown IMAGE_BOOTLOADER value"
            exit 1
        ;;
    esac

    # Burn Partitions
    dd if=${SDIMG_ROOTFS} of=${SDCARD} conv=notrunc seek=1 bs=$(expr ${IMAGE_ROOTFS_ALIGNMENT} \* 1024) && sync && sync
}

IMAGE_CMD_sdcard () {
	if [ -z "${SDCARD_ROOTFS}" ]; then
		bberror "SDCARD_ROOTFS is undefined. To use sdcard image from Ci20 BSP it needs to be defined."
		exit 1
	fi
    
    ROOTFS_SIZE=`du -bks ${SDIMG_ROOTFS} | awk '{print $1}'`
    # Round up RootFS size to the alignment size as well
    echo "RFS size ${ROOTFS_SIZE}"
    SDIMG_SIZE=$(expr ${IMAGE_ROOTFS_ALIGNMENT} + ${ROOTFS_SIZE})

    echo "Creating filesystem with RootFS ${ROOTFS_SIZE_ALIGNED} KiB"
    echo "Creating filesystem total size ${SDIMG_SIZE} KiB"

    # Initialize sdcard image file
    echo "dd if=/dev/zero of=${SDCARD} bs=1 count=0 seek=$(expr 1024 \* ${SDIMG_SIZE})"
    dd if=/dev/zero of=${SDCARD} bs=1 count=0 seek=$(expr 1024 \* ${SDIMG_SIZE})

	${SDCARD_GENERATION_COMMAND}
}

# The sdcard requires the rootfs filesystem to be built before using
# it so we must make this dependency explicit.
IMAGE_TYPEDEP_sdcard = "${@d.getVar('SDCARD_ROOTFS', 1).split('.')[-1]}"

deploy_kernel () {
	rm -f ${IMAGE_ROOTFS}/boot/${KERNEL_IMAGETYPE}*
	cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ${IMAGE_ROOTFS}/boot/
}

ROOTFS_POSTPROCESS_COMMAND += " deploy_kernel ; "

