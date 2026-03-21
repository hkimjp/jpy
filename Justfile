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
    just plus

kill:
    tailwindcss-kill
    kill `lsof -i:${PORT} -t`

run:
    clojure -M:run-m

test:
    clojure -M:dev -m kaocha.runner

# temporary
up:
    java --enable-native-access=ALL-UNNAMED -jar jpy.jar > log/jpy.log 2>log/jpy_error.log &

# temporary, too
down:
    #!/usr/bin/env bash
    kill `lsof -ti:${PORT}`

build:
    clojure -T:build ci

deploy dest: minify build
    ssh {{ dest }} 'mkdir -p jpy jpy/storage'
    scp target/io.github.hkimjp/jpy-*.jar {{ dest }}:jpy/jpy.jar
    scp Justfile {{ dest }}:jpy/
    ssh {{ dest }} 'cd jpy && just down && just up'

stage:
    just deploy ${STAGE}

prod:
    just deploy ${PROD}

clean:
    rm -rf target
