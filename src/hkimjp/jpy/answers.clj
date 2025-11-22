(ns hkimjp.jpy.answers
  (:require
   [hkimjp.datascript :as ds]))

(def list-answers-q
  '[:find ?e ?num
    :in $ ?author
    :where
    [?e :login ?author]
    [?e :p/id ?num]])

(defn list-answers [author]
  (ds/qq list-answers-q author))

; (list-answers "hkimura")
