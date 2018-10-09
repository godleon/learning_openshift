IMAGE_NAME = base-ubuntu1604

.PHONY: build
build:
	docker build -t $(IMAGE_NAME) .

.PHONY: test
test:
	docker build -t $(IMAGE_NAME) .
	IMAGE_NAME=$(IMAGE_NAME) test/run.sh