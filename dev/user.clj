(ns user
  (:require
   [clj-reload.core :as reload]
   [clojure.java.io :as io]
   [charred.api :as charred]
   [environ.core :refer [env]]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.util :as util]
   [hkimjp.jpy.system :refer [start-system stop-system restart-system]]))

;--------------------------

(comment
  (env :develop)
  (defn score [user]
    (ds/qq '[:find ?datetime
             :in $ ?author
             :where
             [?e :login ?author]
             [?e :datetime ?datetime]]
           user))

  (map (fn [x] (-> x first util/mm-dd)) (score "hkimura"))

  (score "hkimura")

  (-> (group-by first scores)
      first)

  (ds/qq '[:find ?author ?datetime
           :where
           [?e :author ?author]
           [?e :datetime ?datetime]])

  (util/list-answers "hkimura")
  :rcf)
;--------------------------
(tel/set-min-level! :debug)
(restart-system)

; (start-system)
; (stop-system)
; (restart-system)

;---------------------------
; clj-reload
; hook does not work?

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
