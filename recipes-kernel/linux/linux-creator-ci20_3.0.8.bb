include recipes-kernel/linux/linux-jz.inc

LINUX_VERSION ?= "3.0.8"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${LINUX_VERSION}:"

KBRANCH = "ci20-v3.0.8"
SRCREV = "c52aae2473591e25305239c4d62c176435f21e31"

SRC_URI = "\
	git://github.com/MIPS/CI20_linux.git;branch=${KBRANCH} \
	file://defconfig \
	file://jz4780_fb.patch \
	"

COMPATIBLE_MACHINE = "(creator-ci20)"
