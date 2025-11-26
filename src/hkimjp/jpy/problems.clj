(ns hkimjp.jpy.problems
  (:require
   [java-time.api :as jt]
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.event :refer [broadcast-message-to-connected-clients!]]
   [hkimjp.jpy.view :refer [page redirect hx]]))

(defn max-num []
  (-> (ds/qq '[:find [(max ?num)]
               :where
               [?e :num ?num]])
      first))

(defn- update-current!
  "datom [?e :current ?id] is only one"
  [id]
  (let [[e _] (ds/qq '[:find [?e ?id]
                       :where
                       [?e :current ?id]])]
    (tel/log! :info (str "current is at " e))
    (ds/put! {:db/id e :current id})
    (broadcast-message-to-connected-clients! "boardcat from server")))

(defn current-id []
  (->> (ds/qq '[:find [?e ?pid]
                :where
                [?e :current ?pid]])
       second))

(defn create!
  [{{:keys [problem]} :params}]
  (tel/log! {:level :info :id "create!" :data {:problem problem}})
  (try
    (let [ret (ds/put! {:valid true
                        :problem problem})
          id ((:tempids ret) -1)]
      (update-current! id)
      (redirect "/admin"))
    (catch Exception e
      (tel/log! {:level :warn :id "create!"
                 :msg (:getMessage e)}))))

(def problems-all
  '[:find ?e ?valid ?problem
    :keys e  valid  problem
    :where
    [?e :valid ?valid]
    [?e :problem ?problem]])

(defn problems
  "list available problems"
  []
  (->> (ds/qq problems-all)
       (sort-by :e)
       reverse))

(defn current! [{{:keys [current]} :params}]
  (let [current (parse-long current)]
    (tel/log! {:level :info :id "current!" :msg (str "current:" current)})
    (update-current! current)
    (redirect "/admin")))
