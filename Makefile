GRADLEW := ./gradlew

.PHONY: build run release clean

## Compile the mod
build:
	$(GRADLEW) build -x test

## Launch Minecraft with the mod loaded (dev environment)
run:
	$(GRADLEW) runClient

## Build and copy the release JAR to the project root
release: build
	@jar=$$(ls build/libs/$(shell grep archives_base_name gradle.properties | cut -d= -f2)-$(shell grep mod_version gradle.properties | cut -d= -f2).jar 2>/dev/null); \
	if [ -z "$$jar" ]; then \
		echo "Release JAR not found in build/libs/"; exit 1; \
	fi; \
	cp "$$jar" .; \
	echo ""; \
	echo "  ✅ Release JAR: $$(basename $$jar)"; \
	echo ""

## Remove all build artifacts
clean:
	$(GRADLEW) clean
