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
    ["" {:get scoreboard/index}]]])

#_(defn root-handler
    [request]
  ; これをやりたいために。
    (t/log! :info (str (:request-method request) " - " (:uri request)))
    (let [handler
          (ring/ring-handler
           (ring/router routes)
           (ring/routes
            (ring/create-resource-handler {:path "/"})
            (ring/create-default-handler
             {:not-found (constantly {:status 404,
                                      :body "<h1>ERROR</h1><p>not found</p>"})
              :method-not-allowed (constantly {:status 405
                                               :body "not allowed"})
              :not-acceptable (constantly {:status 406
                                           :body "not acceptable"})}))
           {:middleware [[wrap-defaults site-defaults]]})]
      (handler request)))

(def root-handler
  (ring/ring-handler
   (ring/router routes)
   (ring/routes
    (ring/create-resource-handler {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly (error-page [:p "not found, check uri"]))
      :method-not-allowed (constantly {:status 405, :body "not allowed"})
      :not-acceptable (constantly {:status 406, :body "not acceptable"})}))
   {:middleware [[wrap-defaults site-defaults]]}))


