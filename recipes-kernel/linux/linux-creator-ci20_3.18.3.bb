include recipes-kernel/linux/linux-jz.inc

LINUX_VERSION ?= "3.18.3"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${LINUX_VERSION}:"

KBRANCH = "ci20-v3.18"
SRCREV = "7dff33297116643485ca37141d804eddd793e834"

SRC_URI = "\
	git://github.com/MIPS/CI20_linux.git;branch=${KBRANCH} \
	file://defconfig \
	"

COMPATIBLE_MACHINE = "(creator-ci20)"
