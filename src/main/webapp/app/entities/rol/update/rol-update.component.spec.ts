import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RolFormService } from './rol-form.service';
import { RolService } from '../service/rol.service';
import { IRol } from '../rol.model';

import { RolUpdateComponent } from './rol-update.component';

describe('Rol Management Update Component', () => {
  let comp: RolUpdateComponent;
  let fixture: ComponentFixture<RolUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rolFormService: RolFormService;
  let rolService: RolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RolUpdateComponent],
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
      .overrideTemplate(RolUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RolUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rolFormService = TestBed.inject(RolFormService);
    rolService = TestBed.inject(RolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const rol: IRol = { id: 456 };

      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      expect(comp.rol).toEqual(rol);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRol>>();
      const rol = { id: 123 };
      jest.spyOn(rolFormService, 'getRol').mockReturnValue(rol);
      jest.spyOn(rolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rol }));
      saveSubject.complete();

      // THEN
      expect(rolFormService.getRol).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rolService.update).toHaveBeenCalledWith(expect.objectContaining(rol));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRol>>();
      const rol = { id: 123 };
      jest.spyOn(rolFormService, 'getRol').mockReturnValue({ id: null });
      jest.spyOn(rolService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rol }));
      saveSubject.complete();

      // THEN
      expect(rolFormService.getRol).toHaveBeenCalled();
      expect(rolService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRol>>();
      const rol = { id: 123 };
      jest.spyOn(rolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rolService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
