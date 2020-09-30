import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import Modeler from 'dmn-js/lib/Modeler';
import {ActivatedRoute, Router} from '@angular/router';
import {RuleVersionService} from '../../services/rule-version.service';

@Component({
  selector: 'app-dmn-editor-page',
  templateUrl: './dmn-editor-page.component.html',
  styleUrls: ['./dmn-editor-page.component.css']
})
export class DmnEditorPageComponent implements OnInit {

  globalModeler;
  dmn = '';
  xml = '';
  id = '';
  versionId = '';

  constructor(
    private http: HttpClient,
    private activatedRoute: ActivatedRoute,
    private ruleVersionService: RuleVersionService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.http.get('../assets/val.xml', {responseType: 'text'}).subscribe(x => {
      this.versionId = window.history.state.id;
      this.dmn = window.history.state.data;
      if (!this.dmn) {
        this.dmn = x;
      }
      const modeler = new Modeler({
        container: '.canvas'
      });
      modeler.importXML(this.dmn, function (err) {
        console.log('*********************');
        if (err) {
          console.log('error rendering', err);
        } else {
          modeler
          .getActiveViewer()
          .get('canvas')
          .zoom('fit-viewport');
        }
      });
      this.globalModeler = modeler;
    });

    this.activatedRoute.params.subscribe(params => {
      this.id = params.id;
    });
  }

  save() {
    let innerXml = '';
    this.globalModeler.saveXML({format: true}, function (error, xml) {
      innerXml = xml;
    });
    this.xml = innerXml;
    this.ruleVersionService.saveXml(this.versionId, this.xml).subscribe(response =>
      this.router.navigate(['project/rule', this.id])
    );
  }
}
