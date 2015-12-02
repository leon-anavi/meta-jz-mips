inherit kernel

DESCRIPTION = "Kernel"
HOMEPAGE = "http://nohomepage.org"
SECTION = "kernel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

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

S = "${WORKDIR}/git"
