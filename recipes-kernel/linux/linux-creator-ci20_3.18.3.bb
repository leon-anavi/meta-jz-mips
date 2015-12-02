include recipes-kernel/linux/linux-jz.inc

LINUX_VERSION ?= "3.18.3"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${LINUX_VERSION}:"

KBRANCH = "ci20-v3.18"
SRCREV = "8e6b668a0c3246161e4d31e040c8a04d948d8032"

SRC_URI = "\
	git://github.com/MIPS/CI20_linux.git;branch=${KBRANCH} \
	file://defconfig \
	"

COMPATIBLE_MACHINE = "(creator-ci20)"
