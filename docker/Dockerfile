FROM ubuntu:18.04

ENV LANG en_US.utf8
ENV LC_ALL en_US.utf8
ENV HOME /home/builder
ENV SSH_AUTH_SOCK /home/builder/.sockets/ssh

RUN apt-get update && apt-get install -y --no-install-recommends \
	asciidoc \
	autogen \
	automake \
	autoconf \
	autopoint \
	gettext \
	ca-certificates \
	cmake \
	bc \
	bison \
	build-essential \
	bzip2 \
	doxygen \
	git \
	gradle \
	lib32stdc++6 \
	lib32z1 \
	libtool \
	locales \
	m4 \
	openjdk-8-jdk \
	pkg-config \
	software-properties-common \
	ssh \
	swig \
	unzip \
	wget \
	curl \
	yasm \
	zip \
	&& locale-gen $LANG $LC_ALL && update-locale $LANG $LC_ALL \
	&& update-java-alternatives --set java-1.8.0-openjdk

# Android SDK tools
RUN wget -O /tmp/android-tools.zip https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip && \
	mkdir -p /opt/android-sdk && \
	unzip -q -d /opt/android-sdk /tmp/android-tools.zip && \
	rm -f /tmp/android-tools.zip && \
	chown -R root:root /opt/android-sdk
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/tools/bin
RUN (while sleep 1; do echo "y"; done) | sdkmanager --update

# Android SDK libraries, NDK
ENV ANDROID_DEPS=\
'build-tools;28.0.3 \
 platforms;android-28 \
 extras;android;m2repository \
 extras;google;m2repository \
 ndk-bundle'
RUN (while sleep 1; do echo "y"; done) | sdkmanager $ANDROID_DEPS
ENV ANDROID_SDK=/opt/android-sdk
ENV ANDROID_NDK=/opt/android-sdk/ndk-bundle

WORKDIR /home/builder/src
VOLUME /home/builder/src
