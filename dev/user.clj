(ns user
  (:require
   [clj-reload.core :as reload]
   ; [clojure.java.io :as io]
   ; [charred.api :as charred]
   ; [environ.core :refer [env]]
   [taoensso.telemere :as tel]
   ; [hkimjp.datascript :as ds]
   ; [hkimjp.jpy.util :as util]
   [hkimjp.jpy.system :refer [start-system stop-system restart-system]]))

;--------------------------
(tel/set-min-level! :debug)

; FIXME: how to set filter?
; (tel/set-ns-filter! {:disallow "hkimjp.datascript.*" :allow "*"})

(restart-system)

; (start-system)

;---------------------------
; clj-reload
; hooks do not work?
(defn before-ns-unload []
  (tel/log! :debug "called before-ns-unload"))

(before-ns-unload)

(defn after-ns-reload []
  (tel/log! :debug "called after-ns-reload"))

(reload/init
 {:output :verbose
  :dirs ["src" "test"]
  :no-reload '#{user}})

(defn reload! []
  (stop-system)
  (reload/reload)
  (start-system))

; (reload!)

;-------------------
