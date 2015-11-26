inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "3.0.8"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${LINUX_VERSION}:"
COMPATIBLE_MACHINE = "(creator-ci20)"
KBRANCH = "ci20-v3.0.8"
SRC_URI = "\
	git://github.com/MIPS/CI20_linux.git;branch=${KBRANCH} \
	file://defconfig \
	file://jz4780_fb.patch \
	"
SRCREV = "c52aae2473591e25305239c4d62c176435f21e31"
PV = "${LINUX_VERSION}+git${SRCPV}"
