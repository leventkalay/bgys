import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Seviye } from 'app/entities/enumerations/seviye.model';
import { Siniflandirma } from 'app/entities/enumerations/siniflandirma.model';
import { Onay } from 'app/entities/enumerations/onay.model';
import { Durumu } from 'app/entities/enumerations/durumu.model';
import { IVarlik, Varlik } from '../varlik.model';

import { VarlikService } from './varlik.service';

describe('Varlik Service', () => {
  let service: VarlikService;
  let httpMock: HttpTestingController;
  let elemDefault: IVarlik;
  let expectedResult: IVarlik | IVarlik[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VarlikService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      adi: 'AAAAAAA',
      yeri: 'AAAAAAA',
      aciklama: 'AAAAAAA',
      gizlilik: Seviye.COK_DUSUK,
      butunluk: Seviye.COK_DUSUK,
      erisebilirlik: Seviye.COK_DUSUK,
      siniflandirma: Siniflandirma.COK_GIZLI,
      onayDurumu: Onay.ONAYLANMADI,
      durumu: Durumu.AKTIF,
      kategoriRiskleri: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Varlik', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Varlik()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Varlik', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          yeri: 'BBBBBB',
          aciklama: 'BBBBBB',
          gizlilik: 'BBBBBB',
          butunluk: 'BBBBBB',
          erisebilirlik: 'BBBBBB',
          siniflandirma: 'BBBBBB',
          onayDurumu: 'BBBBBB',
          durumu: 'BBBBBB',
          kategoriRiskleri: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Varlik', () => {
      const patchObject = Object.assign(
        {
          aciklama: 'BBBBBB',
          gizlilik: 'BBBBBB',
          erisebilirlik: 'BBBBBB',
          siniflandirma: 'BBBBBB',
          onayDurumu: 'BBBBBB',
          kategoriRiskleri: true,
        },
        new Varlik()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Varlik', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          adi: 'BBBBBB',
          yeri: 'BBBBBB',
          aciklama: 'BBBBBB',
          gizlilik: 'BBBBBB',
          butunluk: 'BBBBBB',
          erisebilirlik: 'BBBBBB',
          siniflandirma: 'BBBBBB',
          onayDurumu: 'BBBBBB',
          durumu: 'BBBBBB',
          kategoriRiskleri: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Varlik', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVarlikToCollectionIfMissing', () => {
      it('should add a Varlik to an empty array', () => {
        const varlik: IVarlik = { id: 123 };
        expectedResult = service.addVarlikToCollectionIfMissing([], varlik);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(varlik);
      });

      it('should not add a Varlik to an array that contains it', () => {
        const varlik: IVarlik = { id: 123 };
        const varlikCollection: IVarlik[] = [
          {
            ...varlik,
          },
          { id: 456 },
        ];
        expectedResult = service.addVarlikToCollectionIfMissing(varlikCollection, varlik);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Varlik to an array that doesn't contain it", () => {
        const varlik: IVarlik = { id: 123 };
        const varlikCollection: IVarlik[] = [{ id: 456 }];
        expectedResult = service.addVarlikToCollectionIfMissing(varlikCollection, varlik);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(varlik);
      });

      it('should add only unique Varlik to an array', () => {
        const varlikArray: IVarlik[] = [{ id: 123 }, { id: 456 }, { id: 22590 }];
        const varlikCollection: IVarlik[] = [{ id: 123 }];
        expectedResult = service.addVarlikToCollectionIfMissing(varlikCollection, ...varlikArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const varlik: IVarlik = { id: 123 };
        const varlik2: IVarlik = { id: 456 };
        expectedResult = service.addVarlikToCollectionIfMissing([], varlik, varlik2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(varlik);
        expect(expectedResult).toContain(varlik2);
      });

      it('should accept null and undefined values', () => {
        const varlik: IVarlik = { id: 123 };
        expectedResult = service.addVarlikToCollectionIfMissing([], null, varlik, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(varlik);
      });

      it('should return initial array if no Varlik is added', () => {
        const varlikCollection: IVarlik[] = [{ id: 123 }];
        expectedResult = service.addVarlikToCollectionIfMissing(varlikCollection, undefined, null);
        expect(expectedResult).toEqual(varlikCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
