(ns parenteser.core
  (:require [parenteser.ingest :as ingest]
            [parenteser.pages :as pages]
            [powerpack.app :as app]
            [powerpack.highlight :as highlight]))

(defn create-app []
  (-> {:config
       {:site/base-url "https://parenteser.mattilsynet.io"
        :site/default-language "no"
        :site/title "Parenteser"

        :stasis/build-dir "build"
        :powerpack/content-dir "resources"
        :powerpack/source-dirs ["src" "dev"]
        :powerpack/db "datomic:mem://parenteser"

        :optimus/assets [{:public-dir "public"
                          :paths [#"/images/*.*"
                                  #"/fonts/*"]}]
        :optimus/bundles {"styles.css"
                          {:public-dir "public"
                           :paths ["/css/parenteser.css"]}}

        :powerpack.server/port 5052

        :imagine/config {:prefix "image-assets"
                         :resource-path "public"
                         :disk-cache? true
                         :transformations
                         {:vcard-small
                          {:transformations [[:fit {:width 184 :height 184}]
                                             [:crop {:preset :square}]]
                           :retina-optimized? true
                           :retina-quality 0.4
                           :width 184}}}

        :datomic/schema-resource "schema.edn"}
       :create-ingest-tx #'ingest/create-tx
       :render-page #'pages/render-page}
      app/create-app
      highlight/install))
