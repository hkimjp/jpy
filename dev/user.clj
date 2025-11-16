(ns user
  (:require
   [clj-reload.core :as reload]
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.system :refer [start-system stop-system]]))

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

(tel/set-min-level! :debug)

(start-system)

; -------------------------
(comment
  ;; initialize
  (ds/put! {:num 0
            :problem "dummy problem"
            :stat "ok"
            :datetime (jt/local-date-time)})

  (ds/put! {:current 0})
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

