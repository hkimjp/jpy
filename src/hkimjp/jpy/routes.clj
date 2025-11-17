(ns hkimjp.jpy.routes
  (:require
   [reitit.ring :as ring]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [taoensso.telemere :as t]
   [hkimjp.jpy.admin :as admin]
   [hkimjp.jpy.help :refer [help]]
   [hkimjp.jpy.login :refer [login login! logout!]]
   [hkimjp.jpy.middleware :as m]
   [hkimjp.jpy.scoreboard :as scoreboard]
   [hkimjp.jpy.view :refer [error-page]]
   [hkimjp.jpy.workspace :as workspace]))

(def routes
  [["/"      {:get login :post login!}]
   ["/logout" logout!]
   ["help"   {:get help}]
   ["/admin" {:middleware [m/wrap-admin]}
    [""           {:get admin/admin}]
    ["/create"     {:post admin/create!}]
    ; ["/update/:e"  {:get admin/edit :post admin/upsert!}]
    ; ["/list-all"   {:get admin/list-all}]
    ; ["/delete"     {:post admin/delete!}]
    ]
   ["/workspace" {:middleware [m/wrap-users]}
    ["" {:get workspace/index :post workspace/upload!}]
    ["/answer/:e" {:get workspace/answer}]]
   ["/scoreboard" {:middleware [m/wrap-users]}
    ["" {:get scoreboard/index}]]

   ["/problem"
    ["/create" {:post problem/create1!}]]
   ["/answer"]
   ["/hx"]])

(defn root-handler
  [{:keys [request-method uri] :as request}]
  (t/log! {:level :debug
           :data {:request-method request-method :uri uri}})
  (let [handler
        (ring/ring-handler
         (ring/router routes)
         (ring/routes
          (ring/create-resource-handler {:path "/"})
          (ring/create-default-handler
           {:not-found
            (constantly (error-page [:p "not found, check uri"]))
            :method-not-allowed
            (constantly (error-page [:p "not allowed"]))
            :not-acceptable
            (constantly (error-page [:p "not acceptable"]))}))
         {:middleware [[wrap-defaults site-defaults]]})]
    (handler request)))

;(root-handler {:request-method :get, :uri "/abc"})
