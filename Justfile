set dotenv-load := true

help:
    just --list

CSS := "resources/public/assets/css"

watch:
    tailwindcss -i {{ CSS }}/input.css -o {{ CSS }}/output.css --watch=always

minify:
    tailwindcss -i {{ CSS }}/input.css -o {{ CSS }}/output.css --minify

plus:
    clj -X:dev:plus

nrepl:
    clj -M:dev:nrepl

dev:
    just watch &
    just nrepl


run:
    clojure -M:run-m

test:
    clojure -M:dev -m kaocha.runner


# temporaly
up:
    java --enable-native-access=ALL-UNNAMED -jar jpy.jar &

# not yet
# down:

build:
    clojure -T:build ci

deploy dest: # minify build
    ssh {{dest}} 'mkdir -p jpy jpy/storage'
    scp target/io.github.hkimjp/jpy-*.jar {{ dest }}:jpy/jpy.jar
    scp Justfile {{ dest }}:jpy/
    ssh {{ dest }} 'cd jpy && just up'

stage:
    just deploy ${STAGE}

prod:
    just deploy ${PROD}

clean:
    rm -rf target
