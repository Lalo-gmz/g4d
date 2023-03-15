import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportarComponentComponent } from './importar-component.component';

describe('ImportarComponentComponent', () => {
  let component: ImportarComponentComponent;
  let fixture: ComponentFixture<ImportarComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ImportarComponentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ImportarComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
