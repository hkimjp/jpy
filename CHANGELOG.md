# On Class Python

# Unreleased

* SSE
* todays group (symbol, sign, mark, icon)


# 0.6.1-SNAPSHOT

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
