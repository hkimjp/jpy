# On Class Python

# Unreleased

* change scoreboard `smiles` weekly
* RUFF enable/disabke interactively
* recongize markdown/python code
* namespace for hx
* container with Python?
    python + jvm?
* その人の回答はその日のタグ（？）に。
* & が効いてない。


## HTMX

* htmx@2.0.8
* htmx-ext-sse@2.2.4

# 0.7.1 (2026-03-13)

- improved Justfile

# 0.7.0 (2026-03-13)

- updated reitit-ring

| :file    | :name               | :current | :latest |
|----------|---------------------|----------|---------|
| deps.edn | metosin/reitit-ring | 0.10.0   | 0.10.1  |

- working `just stage`

# 0.7.0 (2026-03-13)

- RUFF = /run/current-system/sw/bin/ruff in `.env`

- updated dependencies

| :file    | :name                                        | :current | :latest |
|----------|----------------------------------------------|----------|---------|
| deps.edn | babashka/fs                                  | 0.5.30   | 0.5.31  |
|          | com.cnuernber/charred                        | 1.037    | 1.038   |
|          | io.github.hkimjp/datascript-storage-javatime | 0.7.6    | 0.7.7   |
|          | io.github.tonsky/clojure-plus                | 1.7.1    | 1.7.2   |

- updated dependencies

| :file    | :name                 | :current | :latest |
|----------|-----------------------|----------|---------|
| deps.edn | com.taoensso/telemere | 1.1.0    | 1.2.0   |
|          | org.clojure/clojure   | 1.12.3   | 1.12.4  |

# 0.6.7 (2025-12-02)

- add ruff validation - only wakes up when a problem starts with "(def"

```
ruff -q format --check <file.py>
```

- simpler timestamp format

# 0.6.6 (2025-12-01)

- /admin/reset - evet/reset-client!
- nginx

```
    location / {
        proxy_http_version         1.1;
        proxy_buffering            off;
    include                    proxy_params;
        proxy_pass                 http://127.0.0.1:8600;
    }
```

# 0.6.5 (2025-11-30)

- removed validate.clj
- vertual thread
- show number of current clients on admin page

# 0.6.4-hotfix

- can not deploy to app.melt
- fixed `Justfile`

# 0.6.3 (2025-11-27)

- scoreboard
- resources/users.{json,txt}
- refactored workspace.clj
- added system/restart-system - (stop-system) (start-system) is better than clj-reload(?)
- updated system/start-server, system/stop-server - only when available, doing the jobs.

# 0.6.2 (2025-11-27)

- admin - no use `true`
- changed answers list: showed eid -> showed 'yy-mm'
- show uploaded timestamp
- changed: ranamed `answers/answer` -> `answers/answer-hx`

# 0.6.1 (2025-11-27)

- can upload answers
- show initial problem
- broadcast current problem
- added resources/public/assets/js/htmx-ext-sse.js
- destructuring middleware.clj/wrap-users, wrap-admin

    * was: (let [user (get-in request [:session :identity])])
    * now: [{{:keys [identity]} :session :as request}]

# 0.6.0 (2025-11-24)

- jar 45MB->40MB
- `org.httpkit.client` - replaced hato, added `charredcom.cnuernber/charred`
- `org.httpkit.server` - replaced jetty
- ring-devel - copied contents of `system.clj` into `user.clj`
- added storage/fetch.sh - copied from konpy2
- added systemd

# 0.5.1 (2025-11-18)

- changed: problem.clj -> problems.clj

# 0.5.0 (2025-11-18)

- refactor, no use `num`.


# 0.4.1 (2025-11-17)

- change `current`

# 0.4.0 (2025-11-17)


# 0.3.1 (2025-11-16)

- learn reitit

# 0.3.0 (2025-11-11)

* display answers - share the functions?
- bump-version-local.sh

# 0.2.2 (2025-11-11)

- redis? no, datascript.
- workspace
- admin

# 0.2.1 (2025-11-10)

- checked ignore list

# 0.2.0 (2025-11-10)

- early deployment. `https://jps.melt.kyutech.ac.jp`

# 0.1.0 (2025-11-10)

- project started
