const AppLayout = () => import(/* webpackChunkName: 'app-layout' */ "@/layouts/app/Default");
const userListe = () => import( /* webpackChunkName: 'user__liste' */ './views/Liste.vue');
//const userDetails = () => import( /* webpackChunkName: 'user__details' */ './views/Details.vue');

const userRoutes = [{
    path: '/app/dashboard/users',
    component: AppLayout,
    children: [
      {
            path: '',
            name: 'user-liste',
            component: () => import( /* webpackChunkName: 'user_liste' */ './views/Liste.vue')
      },
      {
        path: 'users',
        name: 'liste-user',
        component: () => import( /* webpackChunkName: 'user_liste' */ './views/ListeUser.vue')
      },
      {
        path: '/app/dashboard/users/:id',
        name: 'user-details',
        component: () => import( /* webpackChunkName: 'user_details' */ './views/Details.vue'),
      },
      {
        path: '/app/dashboard/users/add',
        name: 'user-add',
        component: () => import( /* webpackChunkName: 'user_add' */ './views/Add.vue')
      },
      {
        path: '/app/dashboard/users/edit/:id',
        name: 'user-edit',
        component: () => import( /* webpackChunkName: 'user_edit */ './views/Edit.vue')
      }
    ]
}];
export default userRoutes;