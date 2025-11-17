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
  [{{:keys [problem]} :params}]
  (tel/log! {:level :info :id "create!" :data {:problem problem}})
  (let [num (-> (max-id) inc)]
    (try
      (ds/put! {:num num
                :valid true
                :problem problem
                :datetime (jt/local-date-time)})
      (ds/put! {:current num})
      (hx [:div.flex.gap-x-4 [:div (str num)] [:div problem]])
      (catch Exception e
        (tel/log! {:level :warn :id "create!"
                   :msg (:getMessage e)})))))

#_(defn update! [])

(def problems-all
  '[:find ?e  ?num ?problem
    :keys e   num  problem
    :where
    [?e :num ?num]
    [?e :problem ?problem]])

(defn problems
  "list available problemslist available problems"
  []
  (->> (ds/qq problems-all)
       (sort-by :num)
       reverse))

; (problems)
