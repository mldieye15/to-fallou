import Middleware from '@/middlewares'
const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const adminRoutes = [{
    path: '/app/dashboard/administrateurs',
    component: AppLayout,
    children: [
      {
        path: 'administrateurs',
        name: 'admin-liste',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/Liste.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/administrateurs/:id',
        name: 'admin-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/administrateurs/add',
        name: 'admin-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/Add.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      },
      {
        path: '/app/dashboard/administrateurs/edit/:id',
        name: 'admin-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue'),
      //   meta: {
      //     middleware: [Middleware.auth]
      // }
      }
    ]
}];
export default adminRoutes;