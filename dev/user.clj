(ns user
  (:require
   [clj-reload.core :as reload]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.system :refer [start-system stop-system]]))

;--------------------------
(tel/set-min-level! :debug)

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

;-------------------
