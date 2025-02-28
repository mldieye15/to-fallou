import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const candidatAuthoriserListe = () => import( /* webpackChunkName: 'candidatAuthoriser__liste' */ './views/Liste.vue');

//const candidatAuthoriserDetails = () => import( /* webpackChunkName: 'candidatAuthoriser__details' */ './views/Details.vue');

const candidatAuthoriserRoutes = [{
    path: '/app/dashboard/candidatAuthorisers',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'candidatAuthorisers-liste',
            component: () => import( /* webpackChunkName: 'candidatAuthoriser_liste' */ './views/Liste.vue'),
            meta: {
              middleware: [Middleware.plannerAuth]
          }
      },
      {
        path: '/app/dashboard/candidatNonAuthoriser',
        name: 'candidatNonAuthorisers-liste',
        component: () => import( /* webpackChunkName: 'candidatAuthoriser_liste' */ './views/ListeExlcu.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
  },
      {
        path: '/app/dashboard/candidatAuthoriser/:id',
        name: 'candidatAuthoriser-details',
        component: () => import( /* webpackChunkName: 'candidatAuthoriser_details' */ './views/Details.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/candidatAuthorisers/add',
        name: 'candidatAuthoriser-add',
        component: () => import( /* webpackChunkName: 'candidatAuthoriser_add' */ './views/Add.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      },
      {
        path: '/app/dashboard/candidatAuthorisers/edit/:id',
        name: 'candidatAuthoriser-edit',
        component: () => import( /* webpackChunkName: 'candidatAuthoriser_edit */ './views/Edit.vue'),
        meta: {
          middleware: [Middleware.plannerAuth]
      }
      }
    ]
}];
export default candidatAuthoriserRoutes;
