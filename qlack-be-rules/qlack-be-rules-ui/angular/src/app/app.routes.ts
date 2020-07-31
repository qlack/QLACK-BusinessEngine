import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ProjectComponent} from './project/project.component';
import {HomeComponent} from './home/home.component';
import {WorkingSetPageComponent} from './pages/working-set-page/working-set-page.component';
import {RulePageComponent} from './pages/rule-page/rule-page.component';
import {CategoryPageComponent} from './pages/category-page/category-page.component';
import {AddWorkingSetComponent} from './add-pages/add-working-set/add-working-set.component';
import {AddRuleComponent} from './add-pages/add-rule/add-rule.component';
import {AddCategoryComponent} from './add-pages/add-category/add-category.component';
import {AddVersionComponent} from './add-pages/add-version/add-version.component';


const routes: Routes = [
  {
    path: 'project', component: ProjectComponent, children:
      [
        {path: 'working-set/:id', component: WorkingSetPageComponent},
        {path: 'working-set/:id/add/:isRule', component: AddVersionComponent},
        {path: 'rule/:id', component: RulePageComponent},
        {path: 'rule/:id/add/:isRule', component: AddVersionComponent},
        {path: 'category/:id', component: CategoryPageComponent},
        {path: 'add-working-set', component: AddWorkingSetComponent},
        {path: 'add-rule', component: AddRuleComponent},
        {path: 'add-category', component: AddCategoryComponent}
      ]
  },
  {path: 'home', component: HomeComponent},
  {path: '**', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutes {
}
