// Composables
import { createRouter, createWebHistory } from 'vue-router'
import Middleware from '@/middlewares'
import academieRoutes from '@/modules/academie/routes';
import villeRoutes from '@/modules/ville/routes';
import juryRoutes from '@/modules/jury/routes';
import anneeRoutes from '@/modules/annee/routes';
import centreRoutes from '@/modules/centre/routes';
import etablissementRoutes from '@/modules/etablissement/routes';
import typeEtablissementRoutes from '@/modules/typeEtablissement/routes';
import sessionRoutes from '@/modules/session/routes';
import typeCentreRoutes from '@/modules/typeCentre/routes';
import demandeRoutes from '@/modules/demande/routes';
import fonctionRoutes from '@/modules/fonction/routes';
import typeSessionRoutes from '@/modules/typeSession/routes';
import codificationRoutes from '@/modules/codifiction/routes';
import userRoutes from '@/modules/user/routes';
import candidatRoutes from '@/modules/candidat/routes';
import adminRoutes from '@/modules/adminstrateurs/administrateur/routes';
import planifRoutes from '@/modules/adminstrateurs/planificateur/routes';
import supRoutes from '@/modules/adminstrateurs/superviseur/routes';
import demandeByVilleRoutes from '@/modules/demandeByVille/routes';
import demandeByCentreRoutes from '@/modules/demandeByCentre/routes';
import candidatAuthoriserRoutes from '@/modules/candidatAuth/routes';
import middlewares from '@/middlewares';
import casRoutes from '@/modules/casSpeciaux/routes';
import demandeSecondaryVilleRoutes from '@/modules/demandeSeciondaryVille/routes';
const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '',
        name: 'home',
        component: () => import(/* webpackChunkName: "home" */ '@/views/Home.vue'),
      },
      {
        path: 'about',
        name: 'about',
        component: () => import(/* webpackChunkName: "about" */ '@/views/public/About.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
          path: 'aide',
          name: 'aide',
          component: () => import ( /* webpackChunkName: "aide" */ '@/views/public/Aide.vue'),
          meta: {
              middleware: [Middleware.guest]
          }
      }
    ],
  },
  {
    path: '/auth',
    component: () => import('@/layouts/auth/Default.vue'),
    children: [{
          path: 'login',
          name: 'login',
          component: () => import ( /* webpackChunkName: "login" */ '@/views/auth/Login.vue'),
          meta: {
              middleware: [Middleware.guest]
          }
      },
      {
        path: 'register',
        name: 'register',
        component: () =>
            import ( /* webpackChunkName: "registre" */'@/views/auth/Register.vue'),
        meta: {
            middleware: [Middleware.guest]
        }
      },
      {
        path:'code',
        name:'code',
        component: ()=>
            import('@/views/auth/RecupererCode.vue'),
        meta: {
          middleware: [Middleware.guest]
        }
      },
      {
        path: 'forgot-password',
        name: 'forgotPassword',
        component: () => import(/* webpackChunkName: "forgotPassword" */ '@/views/auth/ForgotPassword.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
        path: 'reset-password/:token',
        name: 'resetPassword',
        component: () => import(/* webpackChunkName: "resetPassword" */ '@/views/auth/ResetPassword.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
        path: 'verif-token/:token',
        name: 'verifToken',
        component: () => import(/* webpackChunkName: "resetPassword" */ '@/views/auth/VerifToken.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
        path: 'active-compte',
        name: 'activeCompte',
        component: () => import(/* webpackChunkName: "activeCompte" */ '@/views/auth/ActiveCompte.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
        path: 'accessDenied',
        name: 'access-denied',
        component: () => import(/* webpackChunkName: "activeCompte" */ '@/views/auth/AccessDenied.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      },
      {
        path: 'alerte',
        name: 'page',
        component: () => import(/* webpackChunkName: "activeCompte" */ '@/views/auth/Page.vue'),
        meta: {
          middleware: [ Middleware.guest]
        }
      }
    ],
  },
  {
      path: '/app',
      name: 'app',
      component:  () => import('@/layouts/app/Default.vue'),
      children: [{
              path: 'dashboard',
              name: 'dashboard',
              component: () =>
                  import ( /* webpackChunkName: "dashboard" */ '@/views/app/Dashboard.vue'),
              meta: {
                  middleware: [Middleware.supervisorAndAdminAuth]
              }
          },
          {
            path: 'profile',
            name: 'profile',
            component: () =>
                import ( /* webpackChunkName: "profile" */ '@/views/auth/Profile.vue'),
            meta: {
                middleware: [Middleware.supervisorAndAdminAuth]
            }
          },
      ]
  },
  {
    path: '/user',
    name: 'user',
    component:  () => import('@/layouts/user/Default.vue'),
    children: [{
            path: 'accueil',
            name: 'accueil',
            component: () =>
                import ( /* webpackChunkName: "dashboard" */ '@/views/user/Accueil.vue'),
            meta: {
                middleware: [Middleware.userAuth]
            }
        },
        {
          path: 'demande/:id',
          name: 'demande',
          component: () =>
              import ( /* webpackChunkName: "dashboard" */ '@/views/user/Demande.vue'),
          meta: {
              middleware: [Middleware.userAuth]
          }
      },
      {
        path: 'edit/:id',
        name: 'edit',
        component: () =>
            import ( /* webpackChunkName: "dashboard" */ '@/views/user/Edit.vue'),
        meta: {
            middleware: [Middleware.userAuth]
        },

    },
    {
      path: 'candidature/:id',
      name: 'candidaturephase2',
      component: () =>
          import ( /* webpackChunkName: "dashboard" */ '@/views/user/EditPhase2.vue'),
      meta: {
          middleware: [Middleware.userAuth]
      },

  },
        {
          path: 'profile',
          name: 'profileUser',
          component: () =>
              import ( /* webpackChunkName: "profile" */ '@/views/auth/ProfileUser.vue'),
          meta: {
              middleware: [Middleware.userAuth]
          }
        },
    ]
},
  ...academieRoutes,
  ...villeRoutes,
  ...juryRoutes,
  ...anneeRoutes,
  ...centreRoutes,
  ...etablissementRoutes,
  ...typeEtablissementRoutes,
  ...sessionRoutes,
  ...typeCentreRoutes,
  ...demandeRoutes,
  ...fonctionRoutes,
  ...typeSessionRoutes,
  ...codificationRoutes,
  ...userRoutes,
  ...candidatRoutes,
  ...adminRoutes,
  ...demandeByVilleRoutes,
  ...demandeByCentreRoutes,
  ...planifRoutes,
  ...supRoutes,
  ...casRoutes,
  ...demandeSecondaryVilleRoutes,
  ...candidatAuthoriserRoutes
]
const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  linkActiveClass: 'active-link',
  linkExactActiveClass: 'exact-active-link',
  routes,
})

function nextCheck(context, middleware, index) {
  const nextMiddleware = middleware[index];

  if (!nextMiddleware) return context.next;

  return (...parameters) => {
      context.next(...parameters);
      const nextMiddle = nextCheck(context, middleware, index + 1);

      nextMiddleware({
          ...
          context,
          next: nextMiddle
      })
  }
}

router.beforeEach((to, from, next) => {
  if (to.meta.middleware) {
      const middleware = Array.isArray(to.meta.middleware) ? to.meta.middleware : [to.meta.middleware];

      const ctx = {
          from,
          next,
          router,
          to
      }
      const nextMiddleware = nextCheck(ctx, middleware, 1);

      return middleware[0]({
          ...ctx,
          next: nextMiddleware
      })
  }

  return next();
});


export default router
