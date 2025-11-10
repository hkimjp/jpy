(ns hkimjp.jpy.system
  (:require
   [environ.core :refer [env]]
   [ring.adapter.jetty :as jetty]
   [taoensso.telemere :as t]
   [hkimjp.jpy.routes :as routes]
   [hkimjp.datascript :as ds]))

(defonce server (atom nil))

(defn start-jetty
  []
  (let [port (parse-long (or (env :port) "3000"))
        handler (if (some? (env :develop))
                  #'routes/root-handler
                  routes/root-handler)]
    (reset! server (jetty/run-jetty handler {:port port :join? false}))
    (t/log! :info (str "server started at port " port))))

(defn stop-server []
  (when @server
    (.stop @server)
    (t/log! :info "server stopped.")))

(defn start-system []
  (t/log! {:level :info
           :id "start-system"
           :msg (env :develop)
           :data {:datascript (env :datascript)}})
  (try
    (ds/start-or-restore {:url (env :datascript)})
    (start-jetty)
    (catch Exception e
      (t/log! :fatal (.getMessage e))
      (System/exit 0))))

(defn stop-system []
  (stop-server)
  (ds/stop))
