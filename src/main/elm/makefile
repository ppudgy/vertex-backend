## makefile

site=../resource/static/
site_files=index.html Vertex.js



.PHONY: all
all: copy

copy: $(site_files)
	cp index.html $(site)
	cp Vertex.js $(site)


%.js: src/%.elm
	elm make --output $@ $<


clean:
	rm *.js








