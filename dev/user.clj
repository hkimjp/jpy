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

(restart-system)

; (start-system)
; (stop-system)

;---------------------------
; clj-reload
; hooks do not work?

(defn before-ns-unload []
  (println "called before-ns-unload"))

(defn after-ns-reload []
  (println "called after-ns-reload"))

(reload/init
 {:output :verbose
  :dirs ["src" "test"]
  :no-reload '#{user}})

; (reload/reload)

(defn reload []
  (stop-system)
  (reload/reload)
  (start-system))

; (reload)

;-------------------
