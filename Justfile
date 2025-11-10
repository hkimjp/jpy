set dotenv-load

help:
  just --list

CSS := "resources/public/assets/css"

watch:
  tailwindcss -i {{CSS}}/input.css -o {{CSS}}/output.css --watch=always

minify:
  tailwindcss -i {{CSS}}/input.css -o {{CSS}}/output.css --minify

# plus:
#   clj -X:dev:plus

nrepl:
  just watch &
  clj -M:dev:nrepl

dev:
  just nrepl

# container-nrepl:
#   clj -M:dev -m nrepl.cmdline -b 0.0.0.0 -p 5555

# up:
#   docker compose up -d

# down:
#   docker compise down

# nrepl server at 127.0.0.1:1667
# sublime can not connect to bb socket-repl started at 127.0.0.1:1666
# bb:
#   bb nrepl-server

run:
  clojure -M:run-m

test:
  clojure -M:dev -m kaocha.runner

build:
 clojure -T:build ci

deploy: minify build
  scp target/io.github.hkimjp/konpy2-*.jar ${DEST}:konpy2/konpy.jar
  ssh ${DEST} 'sudo systemctl restart konpy'
  ssh ${DEST} 'systemctl status konpy'

update: upgrade
upgrade: force
force:
  clojure -Tantq outdated :upgrade true :force true

clean:
  rm -rf target
