(ns user
  (:require
   [clj-reload.core :as reload]
   [environ.core :refer [env]]
   [org.httpkit.server :as hk]
   [ring.middleware.reload :refer [wrap-reload]]
   [taoensso.telemere :as t]
   [hkimjp.jpy.routes :refer [root-handler]]
   [hkimjp.datascript :as ds]))

;--------------------------
; ring-devel
(defonce server (atom nil))

(defn start-server []
  (let [handler (wrap-reload #'root-handler)]
    (let [port (parse-long (or (env :port) "3000"))]
      (reset! server (hk/run-server handler {:port port}))
      (println (str "http-kit started at port " port)))))

(defn stop-server []
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

(start-system)
; (stop-system)

;---------------------------
; clj-reload
(reload/init
 {:dirs ["src" "test"]
  :no-reload '#{user}
  :unload-hook 'before-unload
  :after-reload 'start-system})

(defn before-unload []
  (stop-system))

(defn after-reload []
  (start-system))

; (reload/reload)

