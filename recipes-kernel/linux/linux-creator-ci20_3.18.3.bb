inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.18.3"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${LINUX_VERSION}:"
COMPATIBLE_MACHINE = "(creator-ci20)"
KBRANCH = "ci20-v3.18"
SRC_URI = "\
	git://github.com/MIPS/CI20_linux.git;branch=${KBRANCH} \
	file://defconfig \
	"
SRCREV = "8e6b668a0c3246161e4d31e040c8a04d948d8032"
PV = "${LINUX_VERSION}+git${SRCPV}"
