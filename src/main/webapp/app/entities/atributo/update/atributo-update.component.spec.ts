import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AtributoFormService } from './atributo-form.service';
import { AtributoService } from '../service/atributo.service';
import { IAtributo } from '../atributo.model';

import { AtributoUpdateComponent } from './atributo-update.component';

describe('Atributo Management Update Component', () => {
  let comp: AtributoUpdateComponent;
  let fixture: ComponentFixture<AtributoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let atributoFormService: AtributoFormService;
  let atributoService: AtributoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AtributoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AtributoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtributoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    atributoFormService = TestBed.inject(AtributoFormService);
    atributoService = TestBed.inject(AtributoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const atributo: IAtributo = { id: 456 };

      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      expect(comp.atributo).toEqual(atributo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoFormService, 'getAtributo').mockReturnValue(atributo);
      jest.spyOn(atributoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributo }));
      saveSubject.complete();

      // THEN
      expect(atributoFormService.getAtributo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(atributoService.update).toHaveBeenCalledWith(expect.objectContaining(atributo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoFormService, 'getAtributo').mockReturnValue({ id: null });
      jest.spyOn(atributoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: atributo }));
      saveSubject.complete();

      // THEN
      expect(atributoFormService.getAtributo).toHaveBeenCalled();
      expect(atributoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAtributo>>();
      const atributo = { id: 123 };
      jest.spyOn(atributoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ atributo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(atributoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
