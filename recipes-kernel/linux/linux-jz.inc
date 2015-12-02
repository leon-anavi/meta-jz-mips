DESCRIPTION = "Kernel"
HOMEPAGE = "http://nohomepage.org"
SECTION = "kernel"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

PV = "${LINUX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"


