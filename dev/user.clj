(ns user
  (:require
   [clj-reload.core :as reload]
   [environ.core :refer [env]]
   ;;[java-time.api :as jt]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :refer [wrap-reload]]
   [taoensso.telemere :as t]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.routes :refer [root-handler]]))

;--------------------------
; ring-devel
(defonce server (atom nil))

(defn start-jetty
  []
  (let [port (parse-long (or (env :port) "3000"))
        handler (wrap-reload #'root-handler)]
    (reset! server (run-jetty handler {:port port :join? false}))
    (t/log! :info (str "server started at port " port))))

(defn stop-jetty []
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
  (stop-jetty)
  (ds/stop))

(t/set-min-level! :debug)
(start-system)

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

