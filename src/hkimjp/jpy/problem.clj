(ns hkimjp.jpy.problem
  (:require
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.view :refer [page redirect hx]]))

(defn max-num []
  (-> (ds/qq '[:find [(max ?num)]
               :where
               [?e :num ?num]])
      first))

(comment
  (ds/qq '[:find ?num
           :where
           [?e :num ?num]])
  (max-num)
  :rcf)

(defn update-current [num]
  (let [[e _] (ds/qq '[:find [?e ?num]
                       :where
                       [?e :current ?num]])]
    (ds/put! {:db/id e :current num})))

(defn current-num []
  (->> (ds/qq '[:find [?e ?num]
                :where
                [?e :current ?num]])
       second))

(comment
  (ds/qq '[:find ?e ?num
           :where
           [?e :current ?num]])

  (update-current 3)
  (current-num)
  (ds/pl 3)
  :rcf)

(defn create!
  [{{:keys [problem]} :params}]
  (tel/log! {:level :info :id "create!" :data {:problem problem}})
  (let [num (-> (max-num) inc)]
    (try
      (ds/put! {:num num
                :valid true
                :problem problem
                :datetime (jt/local-date-time)})
      (update-current num)
      (hx [:div.flex.gap-x-4 [:div (str num)] [:div problem]])
      (catch Exception e
        (tel/log! {:level :warn :id "create!"
                   :msg (:getMessage e)})))))

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

#_(problems)

(defn current! [{{:keys [current]} :params}]
  (let [current (parse-long current)]
    (tel/log! {:level :info :id "current!" :msg (str "current:" current)})
    (update-current current)
    (redirect "/admin")))
