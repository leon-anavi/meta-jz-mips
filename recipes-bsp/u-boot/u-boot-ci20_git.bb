require recipes-bsp/u-boot/u-boot.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

# No patches for other machines yet
COMPATIBLE_MACHINE = "creator-ci20"

BRANCH = "ci20-v2013.10"
SRC_URI = "git://github.com/MIPS/CI20_u-boot;branch=${BRANCH}"

#SRC_URI += "file://0001-mmc-up-spt-text.patch \
#            file://mips_fixup.patch \
#"

SRCREV = "dd3c1b95dac7d10b2ca5806f65e5c1050d7dd0fa"

PV = "${BRANCH}"

LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://README;beginline=2;endline=5;md5=3c0cec9329dbcd30c8b9e7f56a12b71e"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

TARGET_LDFLAGS=""
UBOOT_EXT = "img"

PARALLEL_MAKE=""

UBOOT_MACHINE ?= "${MACHINE}_config"
UBOOT_BINARY = "u-boot.${UBOOT_EXT}"
UBOOT_IMAGE = "u-boot-${MACHINE}-${PV}-${PR}.${UBOOT_EXT}"
UBOOT_SYMLINK = "u-boot-${MACHINE}.${UBOOT_EXT}"
UBOOT_MAKE_TARGET = "all"
UBOOT_SPL = "u-boot-spl.bin"
UBOOT_SPL_IMAGE = "u-boot-${MACHINE}-${PV}-${PR}-spl.bin"



do_configure () {
    oe_runmake -C ${S} O=${B} ${UBOOT_MACHINE}
}

do_compile () {
    oe_runmake -C ${S} O=${B} ${UBOOT_MAKE_TARGET}
}

do_install () {
	install -d ${D}/boot
	install ${B}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
	ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}

	install ${B}/spl/${UBOOT_SPL} ${D}/boot/${UBOOT_SPL_IMAGE}
	ln -sf ${UBOOT_SPL} ${D}/boot/${UBOOT_SPL_IMAGE}

	if [ -e ${WORKDIR}/fw_env.config ] ; then
		install -d ${D}${sysconfdir}
		install -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
	fi

}

FILES_${PN} = "/boot ${sysconfdir}"
# no gnu_hash in uboot.bin, by design, so skip QA
INSANE_SKIP_${PN} = "1"

inherit deploy

addtask deploy before do_package after do_compile

do_deploy () {
	install -d ${DEPLOYDIR}
	install ${B}/${UBOOT_BINARY} ${DEPLOYDIR}/${UBOOT_IMAGE}
	install ${B}/spl/${UBOOT_SPL} ${DEPLOYDIR}/${UBOOT_SPL_IMAGE}

	cd ${DEPLOYDIR}
	rm -f ${UBOOT_BINARY} ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_BINARY}
	
	rm -f ${UBOOT_SPL}
	ln -sf ${UBOOT_SPL_IMAGE} ${UBOOT_SPL}
}
