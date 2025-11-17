(ns hkimjp.jpy.problem
  (:require
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.view :refer [hx]]))

(defn max-id []
  (-> (ds/qq '[:find [(max ?num)]
               :where
               [?e :num ?num]])
      first))

(defn create!
  [{{:keys [problem]} :params :as params}]
  (tel/log! {:level :info :id "create!" :data {:params params}})
  (let [num (-> (max-id) inc)]
    (try
      (ds/put! {:num num
                :avail true
                :problem problem
                :datetime (jt/local-date-time)})
      (ds/put! {:current num})
      (hx [:div.flex.gap-x-4 [:div (str num)] [:div problem]])
      (catch Exception e
        (tel/log! {:level :warn :id "create!"
                   :msg (:getMessage e)})))))

#_(defn update! [])

(def problems-all
  '[:find ?e ?valid ?num ?problem
    :keys e  valid  num  problem
    :where
    [?e :num ?num]
    [?e :valid ?valid]
    [?e :problem ?problem]])

(defn problems
  "list available problemslist available problems"
  []
  (->> (ds/qq problems-all)
       (sort-by :num)
       reverse))

; (problems)
