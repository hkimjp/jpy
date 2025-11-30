# On Class Python

# Unreleased

* change scoreboard `smiles` weekly
* disj clients - heartbeat?
* reset(clear) clients from admin page?

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
