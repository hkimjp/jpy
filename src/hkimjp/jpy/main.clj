(ns hkimjp.jpy.main
  (:gen-class)
  (:require [hkimjp.jpy.system :as system]))

(defn -main [& _args]
  (system/start-system))


