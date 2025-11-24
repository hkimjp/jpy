(ns hkimjp.jpy.system
  (:require
   [environ.core :refer [env]]
   ; [ring.adapter.jetty :refer [run-jetty]]
   [org.httpkit-server :as hk]
   [taoensso.telemere :as t]
   [hkimjp.jpy.routes :refer [root-handler]]
   [hkimjp.datascript :as ds]))

(defonce server (atom nil))

;; (defn start-server
;;   []
;;   (let [port (parse-long (or (env :port) "3000"))]
;;     (reset! server (run-jetty root-handler {:port port :join? false}))
;;     (t/log! :info (str "server started at port " port))))

;; (defn stop-server []
;;   (when @server
;;     (.stop @server)
;;     (t/log! :info "server stopped.")))

(defn start-server
  []
  (let [port (parse-long (or (env :port) "3000"))]
    (reset! server (hk/run-server root-handler {:port port}))))

(defn stop-server
  (@server)
  (reset! server nil))

(defn start-system []
  (t/log! {:level :info
           :id "start-system"
           :msg (env :develop)
           :data {:datascript (env :datascript)}})
  (try
    (ds/start-or-restore {:url (env :datascript)})
    (start-server)
    (catch Exception e
      (t/log! :fatal (.getMessage e))
      (System/exit 0))))

(defn stop-system []
  (stop-server)
  (ds/stop))
