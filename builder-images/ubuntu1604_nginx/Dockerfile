# This image is an Ubuntu base image for Source-to-Image builds
FROM ubuntu:16.04
MAINTAINER godleon <godleon@gmail.com>

LABEL \
    io.k8s.description="Nginx Webserver" \
    io.openshift.tags="builder,webserver,html,nginx" \
    io.openshift.expose-services="8080:http" \
    # Location of the STI scripts inside the image.
    io.openshift.s2i.scripts-url="image:///usr/libexec/s2i"

ENV \
    # Path to be used in other layers to place s2i scripts into
    STI_SCRIPTS_PATH=/usr/libexec/s2i
    #STI_SCRIPTS_PATH=/usr/libexec/s2i \
    # The $HOME is not set by default, but some applications needs this variable
    #HOME=/usr/share/nginx

#RUN shopt -s dotglob

RUN sed -i 's/archive.ubuntu.com/tw.archive.ubuntu.com/g' /etc/apt/sources.list

# This is the list of basic dependencies that all language Docker images can consume.
RUN INSTALL_PKGS="build-essential \
bzip2 \
git \
patch \
tar \
unzip \
wget \
curl \
nginx" && \
export DEBIAN_FRONTEND=noninteractive && \
apt-get -yq update && \
apt-get -yq install $INSTALL_PKGS && \
apt-get clean


# Change the default port for nginx 
# Required if you plan on running images as a non-root user).
RUN sed -i 's/80/8080/' /etc/nginx/nginx.conf
RUN sed -i 's/user nginx;//' /etc/nginx/nginx.conf

# Copy the S2I scripts to /usr/libexec/s2i since we set the label that way
COPY ./.s2i/bin/ $STI_SCRIPTS_PATH

# Setup the 'openshift' user that is used for build execution and for the
# application runtime execution.
#RUN useradd -u 1001 -r -g 0 -d ${HOME} -s /sbin/nologin -c "Default Application User" default && \
#chown -R 1001:0 /usr/share/nginx /var/log/nginx /var/lib/nginx
RUN useradd -u 1001 -r -g 0 -d ${HOME} -s /sbin/nologin -c "Default Application User" openshift-user

RUN touch /run/nginx.pid
RUN chgrp -R 0 /usr/share/nginx /var/log/nginx /var/lib/nginx /run/nginx.pid && \
    chmod -R g=u /usr/share/nginx /var/log/nginx /var/lib/nginx /run/nginx.pid

USER 1001

# Set the default port for applications built using this image
EXPOSE 8080

# Directory with the sources is set as the working directory so all STI scripts
# can execute relative to this path.
#WORKDIR ${HOME}

#ENTRYPOINT ["container-entrypoint"]

# Set the default CMD to print the usage of the language image.
CMD ["$STI_SCRIPTS_PATH/usage"]