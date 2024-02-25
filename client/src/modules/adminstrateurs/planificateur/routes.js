import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const planifRoutes = [{
    path: '/app/dashboard/planificateurs',
    component: AppLayout,
    children: [
      {
        path: 'planificateurs',
        name: 'planif-liste',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/Liste.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/planificateurs/:id',
        name: 'planif-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/planificateurs/add',
        name: 'planif-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/planificateurs/edit/:id',
        name: 'planif-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default planifRoutes;