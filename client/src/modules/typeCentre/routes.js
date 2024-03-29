import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const typeCentreListe = () => import( /* webpackChunkName: 'typeCentre__liste' */ './views/Liste.vue');
//const typeCentreDetails = () => import( /* webpackChunkName: 'typeCentre__details' */ './views/Details.vue');

const typeCentreRoutes = [{
    path: '/app/dashboard/typeCentres',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'typeCentre-liste',
            component: () => import( /* webpackChunkName: 'typeCentre_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/typeCentres/:id',
        name: 'typeCentre-details',
        component: () => import( /* webpackChunkName: 'typeCentre_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/typeCentres/add',
        name: 'typeCentre-add',
        component: () => import( /* webpackChunkName: 'typeCentre_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/typeCentres/edit/:id',
        name: 'typeCentre-edit',
        component: () => import( /* webpackChunkName: 'typeCentre_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default typeCentreRoutes;