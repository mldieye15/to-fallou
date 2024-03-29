import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const typeEtablissementListe = () => import( /* webpackChunkName: 'typeEtablissement__liste' */ './views/Liste.vue');
//const typeEtablissementDetails = () => import( /* webpackChunkName: 'typeEtablissement__details' */ './views/Details.vue');

const typeEtablissementRoutes = [{
    path: '/app/dashboard/typeEtablissements',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'typeEtablissement-liste',
            component: () => import( /* webpackChunkName: 'typeEtablissement_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/typeEtablissements/:id',
        name: 'typeEtablissement-details',
        component: () => import( /* webpackChunkName: 'typeEtablissement_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/typeEtablissements/add',
        name: 'typeEtablissement-add',
        component: () => import( /* webpackChunkName: 'typeEtablissement_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/typeEtablissements/edit/:id',
        name: 'typeEtablissement-edit',
        component: () => import( /* webpackChunkName: 'typeEtablissement_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default typeEtablissementRoutes;