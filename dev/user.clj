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
  (ds/put! {:num 0
            :problem "dummy problem"
            :avail "yes"
            :datetime (jt/local-date-time)})

  (ds/pl 3)

  (ds/qq '[:find ?num ?problem
           :where
           [?e :num ?num]
           [?e :problem ?problem]])

  :rcf)

