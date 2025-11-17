(ns hkimjp.jpy.problem
  (:require
   [hkimjp.datascript :as ds]))

(defn create! [])

(defn update! [])

(def problems-q
  '[:find ?num ?problem
    :keys num problem
    :where
    [?e :num ?num]
    [?e :problem ?problem]])

(defn problems
  "list available problemslist available problems"
  []
  (->> (ds/qq problems-q)
       (sort-by :num)
       reverse))

(problems)
