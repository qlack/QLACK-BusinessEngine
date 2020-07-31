import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexModule} from '@angular/flex-layout';
import {MatTableModule} from '@angular/material/table';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {ProjectComponent} from './project/project.component';
import {AppRoutes} from './app.routes';
import {HomeComponent} from './home/home.component';
import {MatCardModule} from '@angular/material/card';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatTreeModule} from '@angular/material/tree';
import {MatListModule} from '@angular/material/list';
import {RulePageComponent} from './pages/rule-page/rule-page.component';
import {MatRadioModule} from '@angular/material/radio';
import {MatInputModule} from '@angular/material/input';
import {AddRuleComponent} from './add-pages/add-rule/add-rule.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatSelectModule} from '@angular/material/select';
import {AddCategoryComponent} from './add-pages/add-category/add-category.component';
import {AddWorkingSetComponent} from './add-pages/add-working-set/add-working-set.component';
import {CategoryPageComponent} from './pages/category-page/category-page.component';
import {WorkingSetPageComponent} from './pages/working-set-page/working-set-page.component';
import {MatMenuModule} from '@angular/material/menu';
import {ConfirmPopupComponent} from './confirm-popup/confirm-popup.component';
import {MatDialogModule} from '@angular/material/dialog';
import {AddVersionComponent} from './add-pages/add-version/add-version.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AddRulePopupComponent} from './add-pages/add-rule-popup/add-rule-popup.component';
import {MatExpansionModule} from '@angular/material/expansion';
import {HttpClientModule} from '@angular/common/http';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {AceEditorModule} from 'ng2-ace-editor';

@NgModule({
  declarations: [
    AppComponent,
    ProjectComponent,
    HomeComponent,
    RulePageComponent,
    AddRuleComponent,
    AddCategoryComponent,
    AddWorkingSetComponent,
    CategoryPageComponent,
    WorkingSetPageComponent,
    ConfirmPopupComponent,
    AddVersionComponent,
    AddRulePopupComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FlexModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    AppRoutes,
    MatCardModule,
    MatSidenavModule,
    MatTreeModule,
    MatListModule,
    MatRadioModule,
    MatInputModule,
    MatToolbarModule,
    MatSelectModule,
    MatMenuModule,
    ReactiveFormsModule,
    MatExpansionModule,
    HttpClientModule,
    AceEditorModule,
    FormsModule,
    MatCheckboxModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
