(ns hkimjp.jpy.problems
  (:require
   [taoensso.telemere :as tel]
   [hkimjp.datascript :as ds]
   [hkimjp.jpy.event :refer [broadcast-message-to-connected-clients!]]
   [hkimjp.jpy.util :as util]))

(defn- update-current!
  "datom [?e :current ?id] is an only one"
  [id]
  (let [{:keys [e]} (util/current-problem)]
    (tel/log! :info (str "current is at " e))
    (ds/put! {:db/id e :current id})
    (broadcast-message-to-connected-clients! (:problem (util/current-problem)))))

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

(defn problems
  "list available problems"
  []
  (->> (util/problems-all)
       (sort-by :e)
       reverse))

(defn current! [{{:keys [current]} :params}]
  (let [current (parse-long current)]
    (tel/log! {:level :info :id "current!" :msg (str "current:" current)})
    (update-current! current)
    (redirect "/admin")))
