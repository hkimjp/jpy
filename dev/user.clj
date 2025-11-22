(ns user
  (:require
   [clj-reload.core :as reload]
   [environ.core :refer [env]]
   [java-time.api :as jt]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :refer [wrap-reload]]
   [taoensso.telemere :as t]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.routes :refer [root-handler]]))

;--------------------------
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
(reload/init
 {:dirs ["src" "dev" "test"]
  :no-reload '#{user}
  :unload-hook 'before-unload
  :after-reload 'start-system})

(defn before-unload []
  (stop-system))

(defn after-reload []
  (start-system))

; (reload/reload)

; -------------------------

; -------------------------

(comment
  ;; initialize
  (ds/put! {:problem "dummy problem"
            :stat "ok"
            :datetime (jt/local-date-time)})
  (ds/put! {:current 0})
  ;;
  (ds/qq '[:find ?e
           :where
           [?e _ _]])
  (ds/pl 1)
  (ds/pl 2)
  (ds/pl 3)
  (ds/pl 4)
  (ds/pl 5)
  :rcf)

(comment
  (ds/qq '[:find ?e ?num
           :where
           [?e :current ?num]])

  (ds/pl 1)

  (ds/qq '[:find ?problem
           :where
           [?e :num 19]
           [?e :problem ?problem]])

  (ds/qq '[:find ?e
           :where
           [?e _ _]])

  (ds/pl 5)

  :rcf)

