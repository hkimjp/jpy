(ns hkimjp.jpy.system
  (:require
   [environ.core :refer [env]]
   [org.httpkit.server :as hk]
   [ring.middleware.reload :refer [wrap-reload]]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.routes :refer [root-handler]]
   [hkimjp.datascript :as ds]))

(defonce server (atom nil))

(defn start-server []
  (when-not @server
    (let [port (parse-long (or (env :port) "3000"))
          handler (if (env :develop)
                    (do
                      (tel/log! :debug "wrap-reload #'root-handler")
                      (wrap-reload #'root-handler))
                    root-handler)]
      (reset! server (hk/run-server handler {:port port}))
      (tel/log! :info (str "server started at port " port)))))

(defn stop-server []
  (when (some? @server)
    (@server)
    (reset! server nil)))

(defn start-system []
  (tel/log! {:level :info
             :id "start-system"
             :msg (env :develop)
             :data {:datascript (env :datascript)}})
  (try
    (ds/start-or-restore {:url (env :datascript)})
    (start-server)
    (catch Exception e
      (tel/log! :fatal (.getMessage e))
      (System/exit 0))))

(defn stop-system []
  (stop-server)
  (ds/stop))

(defn restart-system []
  (stop-system)
  (start-system))
