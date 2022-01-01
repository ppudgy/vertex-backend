## makefile

site=../resources/static/
site_files=index.html Main.js



.PHONY: all
all: copy

copy: $(site_files)
	cp index.html $(site)
	cp Main.js $(site)


%.js: src/%.elm
	elm make --output $@ $<


clean:
	rm *.js








